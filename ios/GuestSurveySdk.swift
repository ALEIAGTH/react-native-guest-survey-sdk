import Geo4CastSDK
import ConfirmitMobileSDK
import CoreFoundation

@objc(GuestSurveySdk)
class GuestSurveySdk: NSObject {

    @objc static func requiresMainQueueSetup() -> Bool {
        return false
    }
    
    @objc(configure:language:enableDebugging:callback:)
    func configure(secretKey:String, language:String, enableDebugging:Bool,callback: @escaping RCTResponseSenderBlock){
        DispatchQueue.main.async {
            Geo4Cast.configure(secretKey: secretKey, language: language) { completed in
                Geo4Cast.shared.enableDebugLogging = enableDebugging
                do{
                    ConfirmitSDK.enableLog(enable: enableDebugging)
                    ConfirmitSDK.Setup().configure()
                    if let clientId = Geo4Cast.shared.surveyVendorClientID, let secretKey = Geo4Cast.shared.surveyVendorClientSecret{
                        try ConfirmitServer.configureUS(clientId: clientId, clientSecret: secretKey)
                        callback([completed])
                    }
                }catch(let error){
                    callback([completed,error])
                }
            }
        }
    }
    
    @objc(startCollection:)
    func startCollection(_ userId:String)->Void{
        DispatchQueue.main.async {
            Geo4Cast.shared.startCollection(with: userId)
        }
    }
    
    @objc(stopCollection)    
    func stopCollection()->Void{
        DispatchQueue.main.async {
            Geo4Cast.shared.stopCollection()
        }
    }
    
    @objc(getUserSurveys:)
    func getUserSurveys(_ callback: @escaping RCTResponseSenderBlock){
        Geo4Cast.shared.getUserSurveys { (data, error) in
            if let data=data{
                return callback([data])
            }
            guard let error = error else{
                return callback(["unknown error"])
            }
            return callback([data as Any,error])
        }
    }
    
    ///Trigger the survey with the programKey and eventName created by the survey studio
    @objc(triggerSurvey:withEvent:)
    func triggerSurvey(programKey:String, withEvent eventName:String)->Void{
        guard let server = ConfirmitServer.us else{
            return
        }
        let serverId = server.serverId
        let programKey = programKey
        SurveySDK.setUniqueIdProvider(provider: AppUniqueDeviceIdProvider())
        TriggerSDK.download(serverId: serverId, programKey: programKey)  { success, error in
            if success{
                print("success")
                //                    feedbackManager.setAllDelegate()
                DispatchQueue.main.async {
                    let feedbackManager = UIWebConfirmit()
                    TriggerSDK.setCallback(serverId: serverId, programKey: programKey, callback: feedbackManager)
                    TriggerSDK.notifyEvent(serverId: ConfirmitServer.us.serverId, programKey: programKey, event: eventName)
                }
            }else if let error = error{
                print("error:",error)
            }else{
                print("unknown error")
            }
        }
    }
    
    @objc(addPushNotificationToken:)
    func addPushNotificationToken(token:String){
        Geo4Cast.shared.addPushNotificationToken(token: token)
    }
    
    @objc(sendLog)
    func sendLog(){
        Geo4CastSDK.Geo4Cast.sendLog()
    }
}






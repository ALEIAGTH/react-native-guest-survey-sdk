import Geo4CastSDK
import ConfirmitMobileSDK
import CoreFoundation

@objc(GuestSurveySdk)
class GuestSurveySdk: NSObject {
    @objc static func requiresMainQueueSetup() -> Bool {
        return false
    }
    
    @objc(configure:programKey:language:enableDebugging:callback:)
    func configure(secretKey:String, programKey:String, language:String, enableDebugging:Bool,callback: @escaping RCTResponseSenderBlock){
        DispatchQueue.main.async {
            Geo4Cast.configure(secretKey: secretKey, language: language) { completed in
                Geo4Cast.shared.enableDebugLogging = enableDebugging
                do{
                    Geo4Cast.shared.setUserVariable(key: "user-language", value: language)
                    ConfirmitSDK.enableLog(enable: enableDebugging)
                    ConfirmitSDK.Setup().rootPath(path: nil).configure()
                    SurveySDK.setUniqueIdProvider(provider: AppUniqueDeviceIdProvider())
                    if let clientId = Geo4Cast.shared.surveyVendorClientID, let secretKey = Geo4Cast.shared.surveyVendorClientSecret{
                        try ConfirmitServer.configureUS(clientId: clientId, clientSecret: secretKey)
                        DispatchQueue.main.async {
                            let feedbackManager = UIWebConfirmit()
                            feedbackManager.setAllDelegate()
                        }
                        TriggerSDK.download(serverId: ConfirmitServer.us.serverId, programKey: programKey)  { success, error in
                            if success{
                                callback([completed])
                            }else if let error = error{
                                callback([completed,error])
                            }
                        }
                    }else{
                        let error = NSError(domain: "Geo4CastSDKError", code: 9999, userInfo: nil)
                        callback([completed,error])
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
    @objc(triggerSurvey:withEvent:parameters:)
    func triggerSurvey(programKey:String, withEvent eventName:String, parameters:Any?)->Void{
        guard var parameters = parameters as? Dictionary<String,String?> else {
            return
        }
        parameters["userId"] = Geo4Cast.shared.userId

        DispatchQueue.main.async {
            TriggerSDK.notifyEvent(serverId: ConfirmitServer.us.serverId, programKey: programKey, event: eventName, data: parameters)
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






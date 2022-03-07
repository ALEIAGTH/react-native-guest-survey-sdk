//
//  UIWebConfirmit.swift
//  GuestSurveySdk
//
//  Created by it4pme office on 16/06/2021.
//  Copyright © 2021 Facebook. All rights reserved.
//

import Foundation
import ConfirmitMobileSDK
import Geo4CastSDK

class UIWebConfirmit: UIViewController, ProgramCallback{
    
    private var startedSurveyData:[String: String?] = [:]
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    // ProgramCallback implementation starts
    
    func onWebSurveyStart(surveyWebView: SurveyWebViewController) {
        self.startedSurveyData = surveyWebView.customData
        DispatchQueue.main.async {
            if let topView = UIApplication.getTopViewController(){
                surveyWebView.delegate = self
                topView.present(surveyWebView, animated: true)
            }
        }
    }
    
    func onSurveyStart(config: SurveyFrameConfig) {
        // This is used when implement survey with native UI
        // For implementation detail, please check "Native UI Survey Integration" section
    }
    
    func onSurveyDownloadCompleted(triggerInfo: TriggerInfo, surveyId: String, error: Error?) {
        // Survey download completed
        guard let error = error else{
            print("survey id: \(surveyId) finished")
            return
        }
        print(error)
    }
    
    func onScenarioLoad(triggerInfo: TriggerInfo, error: Error?) {
        // On scenario script starts
    }
    
    func onScenarioError(triggerInfo: TriggerInfo, error: Error) {
        // When scenario scripting contains error
    }
    
    func onAppFeedback(triggerInfo: TriggerInfo, data: [String: String?]) {
        // When AppFeedback action triggered
    }
}

extension UIWebConfirmit: SurveyWebViewControllerDelegate {
    func onWebViewSurveyError(serverId: String, projectId: String, error: Error) {
        print("web view load error", error)
    }
    
    func onWebViewSurveyFinished(serverId: String, projectId: String) {
        print("survey id: \(projectId) finished")
        guard let eventName = self.startedSurveyData["event"] as? String else{
            return
        }
        Geo4Cast.shared.updateSurveyStatus(status: 1,eventName: eventName) { (finished, error) in
            print("survey has completed with eventName \(projectId)")
        }
    }
    
    func onWebViewSurveyQuit(serverId: String, projectId: String) {
        print("survey id: \(projectId) closed")
    }
}

extension UIWebConfirmit{
    
    func getPrograms() -> [Program] {
        var programs: [Program] = []
        let servers = try! ConfirmitServer.getServers()
        for server in servers {
            do {
                let program = try TriggerSDK.getPrograms(serverId: server.serverId)
                programs += program
            } catch {
                // server doesn't exist
            }
        }
        return programs.sorted(by: { (p1, p2) -> Bool in
            p1.serverId > p2.serverId
        })
    }
    
    func setAllDelegate() {
        for program in getPrograms() {
            setDelegate(serverId: program.serverId, programKey: program.programKey)
        }
    }
    
    func setDelegate(serverId: String, programKey: String) {
        TriggerSDK.setCallback(serverId: serverId, programKey: programKey, callback: self)
    }
    
}


class AppUniqueDeviceIdProvider: UniqueIdProvider {
    func getUniqueId() -> String {
        return Geo4Cast.shared.userId
    }
}

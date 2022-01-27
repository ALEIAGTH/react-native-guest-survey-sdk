//
//  Helper+Extensions.swift
//  GuestSurveySdk
//
//  Created by it4pme office on 16/06/2021.
//  Copyright © 2021 Facebook. All rights reserved.
//

import Foundation
internal extension UIApplication {
    @objc class func getTopViewController(base: UIViewController? = UIApplication.shared.keyWindow?.rootViewController) -> UIViewController? {

        if let nav = base as? UINavigationController {
            return getTopViewController(base: nav.visibleViewController)

        } else if let tab = base as? UITabBarController, let selected = tab.selectedViewController {
            return getTopViewController(base: selected)

        } else if let presented = base?.presentedViewController {
            return getTopViewController(base: presented)
        }
        return base
    }
}

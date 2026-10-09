import SwiftUI
import UIKit
import Firebase
import FirebaseCrashlytics
import UserNotifications

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        FirebaseApp.configure()
        let center = UNUserNotificationCenter.current()
        center.delegate = self
        center.requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
            if let error = error {
                NSLog("AppDelegate: Notification authorization failed: \(error.localizedDescription)")
            } else {
                NSLog("AppDelegate: Notification authorization granted: \(granted)")
            }
        }
        return true
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        NSLog("AppDelegate: Presenting foreground notification \(notification.request.identifier)")
        completionHandler([.banner, .list, .sound])
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        let identifier = response.notification.request.identifier
        if let deepLink = response.notification.request.content.userInfo["deep_link"] as? String,
           let url = URL(string: deepLink) {
            NSLog("AppDelegate: Notification \(identifier) tapped, opening \(deepLink)")
            UIApplication.shared.open(url)
        } else {
            NSLog("AppDelegate: Notification \(identifier) tapped, but has no valid deep link")
        }
        completionHandler()
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

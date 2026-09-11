import UIKit
import SwiftUI
import ComposeApp
import Combine

struct ComposeView: UIViewControllerRepresentable {
    let deepLinkUrl: String?

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(deepLinkUrl: deepLinkUrl)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    @State private var deepLinkUrl: String?

    var body: some View {
        ComposeView(deepLinkUrl: deepLinkUrl)
            .id(deepLinkUrl ?? "root")
            .ignoresSafeArea()
            .onOpenURL { url in
                deepLinkUrl = url.absoluteString
            }
            .onReceive(NotificationCenter.default.publisher(for: .openTimetableDeepLink)) { notification in
                if let deepLink = notification.object as? String {
                    deepLinkUrl = deepLink
                }
            }
    }
}



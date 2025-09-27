package com.ubb.fmi.orar.ui.catalog.extensions

import com.ubb.fmi.orar.ui.catalog.model.ToastLength
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.darwin.DISPATCH_TIME_NOW
import platform.darwin.NSEC_PER_SEC
import platform.darwin.dispatch_after
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_time

private const val LENGTH_LONG_SECONDS = 3L
private const val LENGTH_SHORT_SECONDS = 2L

/**
 * Ios specific implementation of function to show toast
 */
actual fun showToast(context: Any?, message: String, length: ToastLength) {
    val rootController = UIApplication.sharedApplication.keyWindow?.rootViewController ?: return
    val alert = UIAlertController.alertControllerWithTitle(
        title = null,
        message = message,
        preferredStyle = UIAlertControllerStyleAlert
    )

    rootController.presentViewController(alert, animated = true, completion = null)

    val delay = when (length) {
        ToastLength.LONG -> LENGTH_LONG_SECONDS * NSEC_PER_SEC.toLong()
        ToastLength.SHORT -> LENGTH_SHORT_SECONDS * NSEC_PER_SEC.toLong()
    }

    dispatch_after(
        dispatch_time(DISPATCH_TIME_NOW, delay),
        dispatch_get_main_queue()
    ) {
        alert.dismissViewControllerAnimated(true, completion = null)
    }
}
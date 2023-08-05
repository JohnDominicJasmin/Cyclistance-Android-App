package com.example.cyclistance.feature_messaging.domain.use_case.notification

import com.example.cyclistance.feature_messaging.domain.model.SendNotificationModel
import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository

class SendNotificationUseCase(private val repository: MessagingRepository) {
   suspend operator fun invoke(model: SendNotificationModel){
       repository.sendNotification(model = model)
   }
}
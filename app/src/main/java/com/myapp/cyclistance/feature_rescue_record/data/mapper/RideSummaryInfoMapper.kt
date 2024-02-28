package com.myapp.cyclistance.feature_rescue_record.data.mapper

import com.myapp.cyclistance.feature_rescue_record.data.local.entities.RideSummaryInfo
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideSummary

object RideSummaryInfoMapper {

        fun RideSummary.toRideSummaryInfo(): RideSummaryInfo {
            return RideSummaryInfo(
                rating = this.rating,
                ratingText = this.ratingText,
                iconDescription = this.iconDescription,
                bikeType = this.bikeType,
                date = this.date,
                startingTime = this.startingTime,
                endTime = this.endTime,
                startingAddress = this.startingAddress,
                destinationAddress = this.destinationAddress,
                duration = this.duration
            )
        }

        fun RideSummaryInfo.toRideSummary(): RideSummary {
            return RideSummary(
                rating = this.rating,
                ratingText = this.ratingText,
                iconDescription = this.iconDescription,
                bikeType = this.bikeType,
                date = this.date,
                startingTime = this.startingTime,
                endTime = this.endTime,
                startingAddress = this.startingAddress,
                destinationAddress = this.destinationAddress,
                duration = this.duration
            )
        }
}
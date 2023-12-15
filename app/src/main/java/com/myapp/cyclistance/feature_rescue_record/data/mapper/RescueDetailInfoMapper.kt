package com.myapp.cyclistance.feature_rescue_record.data.mapper

import com.myapp.cyclistance.feature_rescue_record.data.local.entities.RideDetailInfo
import com.myapp.cyclistance.feature_rescue_record.data.mapper.RideSummaryInfoMapper.toRideSummary
import com.myapp.cyclistance.feature_rescue_record.data.mapper.RideSummaryInfoMapper.toRideSummaryInfo
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideDetails

object RescueDetailInfoMapper {

    fun RideDetails.toRideDetailInfo(): RideDetailInfo {
        return RideDetailInfo(

            rideId = this.rideId,
            rescuerId = this.rescuerId,
            rescuerName = this.rescuerName,
            rescuerPhotoUrl = this.rescuerPhotoUrl,
            rescueeId = this.rescueeId,
            rescueeName = this.rescueeName,
            rescueePhotoUrl = this.rescueePhotoUrl,
            rideDate = this.rideDate,
            rideSummary = this.rideSummary.toRideSummaryInfo()

        )
    }

    fun RideDetailInfo.toRideDetails(): RideDetails {
        return RideDetails(

            rideId = this.rideId,
            rescuerId = this.rescuerId,
            rescuerName = this.rescuerName,
            rescuerPhotoUrl = this.rescuerPhotoUrl,
            rescueeId = this.rescueeId,
            rescueeName = this.rescueeName,
            rescueePhotoUrl = this.rescueePhotoUrl,
            rideDate = this.rideDate,
            rideSummary = this.rideSummary.toRideSummary()

        )
    }
}
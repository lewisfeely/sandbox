package uk.co.timesheets24.app.timesheetssandbox.Models.Conversion

import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.LiveJobLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.RecentEntryLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.LiveJobRemote
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.RecentEntryRemote

class ConvertToLocalData {

     fun convertToLocalLiveJob(liveJob: LiveJobRemote): LiveJobLocal {
        return LiveJobLocal(
            poNumber = liveJob.poNumber.toString(),
            createdDate = liveJob.createdDate,
            description = liveJob.description,
            jobDate = liveJob.jobDate,
            jobGUID = liveJob.jobGUID,
            jobNumber = liveJob.jobNumber,
            clientId = liveJob.clientId.toString(),
            clientName = liveJob.clientName.toString(),
            clientContactId = liveJob.clientContactId.toString(),
            clientContactName = liveJob.clientContactName.toString(),
            addressId = liveJob.addressId.toString(),
            customerSite = liveJob.customerSite.toString(),
            timeTime = liveJob.timeTime.toInt(),
            travellingTime = liveJob.travellingTime,
            overtime = liveJob.overtime,
            readyToClose = liveJob.readyToClose
        )
    }

    fun convertToLocalRecentEntry(recentEntry: RecentEntryRemote) : RecentEntryLocal {
        return RecentEntryLocal (
            jobNumber = recentEntry.jobNumber,
            jobDate = recentEntry.jobDate,
            jobGUID = recentEntry.jobGUID,
            description = recentEntry.description,
            timetake = recentEntry.timetake,
            travellingTime = recentEntry.travellingTime,
            overTime = recentEntry.overTime,
            timeId = recentEntry.timeId,
            clientName = recentEntry.clientName,
            tempId = null
        )
    }

}


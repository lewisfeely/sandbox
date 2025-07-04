package uk.co.timesheets24.app.timesheetssandbox.Models.Conversion

import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.LiveJobsTable
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.LiveJob

class ConvertToLocalData {

     fun convertToLocalLiveJob(liveJob: LiveJob): LiveJobsTable {
        return LiveJobsTable(
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
}
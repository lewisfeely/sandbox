package uk.co.timesheets24.app.timesheetssandbox.Models.Conversion

import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.DashboardLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.JobTimeStatusLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.LiveJobLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.PermissionLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.ProfileLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.LocalData.RecentEntryLocal
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.DashboardRemote
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.JobTimeStatusRemote
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.LiveJobRemote
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.PermissionRemote
import uk.co.timesheets24.app.timesheetssandbox.Models.RemoteData.ProfileRemote
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

    fun convertToLocalRecentEntry(recentEntry : RecentEntryRemote) : RecentEntryLocal {
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

    fun convertToLocalDashboard(dashboard:DashboardRemote) : DashboardLocal {
        return DashboardLocal(
            contactId = dashboard.contactId,
            greetingName = dashboard.greetingName,
            overtime = dashboard.overtime,
            Timetake = dashboard.Timetake,
            travellingTime = dashboard.travellingTime,
            availableTime = dashboard.availableTime,
        )
    }

    fun convertToLocalJobStatus(jobStatus:JobTimeStatusRemote) : JobTimeStatusLocal {
        return JobTimeStatusLocal(
            jobTimeStatusId = jobStatus.jobTimeStatusId,
            description = jobStatus.description
        )
    }

    fun convertToLocalProfile(profile:ProfileRemote) : ProfileLocal  {
        return ProfileLocal(
            id = profile.id,
            title = profile.title,
            firstname = profile.firstname,
            lastname = profile.lastname,
            name = profile.name,
            username = profile.username,
            companyName = profile.companyName,
            departmentName = profile.departmentName,
            email = profile.email,
            telephone = profile.telephone,
            mobile = profile.mobile,
            greetingName = profile.greetingName,
            defaultUrl = profile.defaultUrl,
            avatar = profile.avatar,
            status = profile.status,
            setUpCompleted = profile.setUpCompleted
        )
    }

    fun convertToLocalPermission(permission:PermissionRemote) : PermissionLocal {
        return PermissionLocal(
            permissionID = permission.permissionID,
            permissionGroupId = permission.permissionGroupId,
            permissionDescription = permission.permissionDescription,
            permissionGroupDescription = permission.permissionGroupDescription,
            internalReference = permission.internalReference
        )
    }

}




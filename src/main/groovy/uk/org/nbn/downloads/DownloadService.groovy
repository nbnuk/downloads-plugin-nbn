package uk.org.nbn.downloads

import au.org.ala.downloads.DownloadParams
import grails.transaction.Transactional
import grails.util.Named
import org.springframework.beans.factory.annotation.Qualifier

import javax.annotation.PostConstruct

//@Transactional
class DownloadService extends au.org.ala.downloads.DownloadService{

    DownloadService(grailsApplication){
        super.setGrailsApplication(grailsApplication)
    }

    @PostConstruct
    void init() {
        def ctx = grailsApplication.mainContext
        setWebService(ctx.getBean("webService"))
        setBiocacheService(ctx.getBean("biocacheService"))
    }

    @Override
    Map triggerDownload(DownloadParams downloadParams) throws Exception {
        if (downloadParams.downloadType == DownloadType.RECORDS.type) {
            // set some defaults
            downloadParams.file = downloadParams.file ?: downloadParams.downloadType + "-" + new Date().format("yyyy-MM-dd")
            // catch different formats
            if (downloadParams.downloadFormat == DownloadFormat.DWC.format) {
                // DwC download
                if (grailsApplication.config.downloads?.dwcFields ?: "") { //NBN: overwrite
                    downloadParams.fields = grailsApplication.config.downloads?.dwcFields
                } else {
                    downloadParams.fields = biocacheService.getDwCFields()
                }
                triggerOfflineDownload(downloadParams)
            }
        } else if (downloadParams.downloadType == DownloadType.MAP.type) {
            DownloadService.log.info "${DownloadType.MAP.type} download triggered"
            DownloadService.log.info "map downloadParams = ${downloadParams}"
            triggerOfflineDownload(downloadParams)
        } else {
            super.triggerDownload(downloadParams)
        }
    }
}


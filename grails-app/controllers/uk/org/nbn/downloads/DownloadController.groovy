package uk.org.nbn.downloads

import au.org.ala.downloads.DownloadParams
import au.org.ala.downloads.DownloadType

class DownloadController extends au.org.ala.downloads.DownloadController{

//    @Override
    def options1(NbnDownloadParams downloadParams) {
        if (!downloadParams.file) {
            downloadParams.file = DownloadType.RECORDS.type + "-" + new Date().format("yyyy-MM-dd")
        }
        request.setAttribute("mapLayoutParams",downloadParams.mapLayoutParams)
        super.options1(downloadParams)
    }

    def options2(NbnDownloadParams downloadParams) {
        request.setAttribute("mapLayoutParams",downloadParams.mapLayoutParams)
        super.options2(downloadParams)
    }
}

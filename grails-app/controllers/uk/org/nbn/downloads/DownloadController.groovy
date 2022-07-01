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


        if (downloadParams.downloadType == DownloadType.MAP.type) {
            downloadParams.email = authService?.getEmail() ?: downloadParams.email // if AUTH is not present then email should be populated via input on page

            def queryContext = grailsApplication.config.biocache?.queryContext?:""
            if (queryContext > '') {
                downloadParams.searchParams = downloadParams.searchParams + '&fq=' + URLEncoder.encode(queryContext, "UTF-8")
            }
            def json = downloadService.triggerDownload(downloadParams)
            log.info "map download json = ${json}"
            chain (action:'confirm', model: [
                    isQueuedDownload: true,
                    downloadParams: downloadParams,
                    json: json // Map
            ], params:[searchParams: downloadParams.searchParams, targetUri: downloadParams.targetUri, downloadType: downloadParams.downloadType, mapLayoutParams: downloadParams.mapLayoutParams])

        }
        super.options2(downloadParams)
    }

    def confirm (DownloadParams downloadParams) {

        Map returnModel = super.confirm(downloadParams)
        if (chainModel){
            if (downloadParams.downloadType == DownloadType.CHECKLIST.type) {
                chainModel.downloadUrl = replaceFacet(chainModel.downloadUrl)
            } else if (downloadParams.downloadType == DownloadType.FIELDGUIDE.type) {
                chainModel.downloadUrl = replaceFacet(chainModel.downloadUrl)
                chainModel.json = replaceFacet(chainModel.json)
            }
        }

        returnModel;

    }

    protected def replaceFacet(String url){
        String facetToReplace = "species_guid"
        if (grailsApplication.config.downloads?.checklistFacet) {
            if (grailsApplication.config.downloads.checklistFacet != facetToReplace) {
                String stringToReplace = "&facets=" + facetToReplace

                def lastIndex = url.lastIndexOf(stringToReplace)
                if (lastIndex > 0) {
                    url = url.substring(0, lastIndex) + "&facets=" + grailsApplication.config.downloads.checklistFacet + url.substring(lastIndex + stringToReplace.length(), url.length())
                }
            }
        }

        return url
    }
}

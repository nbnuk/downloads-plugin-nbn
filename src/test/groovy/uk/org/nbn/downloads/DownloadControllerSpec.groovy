package uk.org.nbn.downloads

import au.org.ala.downloads.DownloadParams
import au.org.ala.downloads.DownloadType
import au.org.ala.downloads.DownloadService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */

@TestFor(DownloadController)
class DownloadControllerSpec extends Specification {

    private static String ALA_DOWNLOAD_URL = "https://records-ws.nbnatlas.org/occurrences/facets/download?file=records-2022-07-01&reasonTypeId=10&dwcHeaders=true&fileType=csv&qa=none&email=h.manders.jones%40nbn.org.uk&q=lsid%3ANBNSYS0000138878&fq=license%3A%22OGL%22&facets=species_guid&lookup=true&counts=true&lists=true"
    private static String NBN_DOWNLOAD_URL = "https://records-ws.nbnatlas.org/occurrences/facets/download?file=records-2022-07-01&reasonTypeId=10&dwcHeaders=true&fileType=csv&qa=none&email=h.manders.jones%40nbn.org.uk&q=lsid%3ANBNSYS0000138878&fq=license%3A%22OGL%22&facets=taxon_concept_lsid&lookup=true&counts=true&lists=true"

    private static String ALA_DOWNLOAD_URL2 = "https://records-ws.nbnatlas.org/occurrences/facets/download?file=records-2022-07-01&reasonTypeId=10&dwcHeaders=true&fileType=csv&qa=none&email=h.manders.jones%40nbn.org.uk&q=lsid%3ANBNSYS0000138878&fq=license%3A%22OGL%22&facets=species_guid"
    private static String NBN_DOWNLOAD_URL2 = "https://records-ws.nbnatlas.org/occurrences/facets/download?file=records-2022-07-01&reasonTypeId=10&dwcHeaders=true&fileType=csv&qa=none&email=h.manders.jones%40nbn.org.uk&q=lsid%3ANBNSYS0000138878&fq=license%3A%22OGL%22&facets=taxon_concept_lsid"

    private static String ALA_BASE_DOWNLOAD_URL = "https://records-ws.nbnatlas.org/occurrences/facets/download?file=records-2022-07-01&reasonTypeId=10&dwcHeaders=true&fileType=csv&qa=none&email=h.manders.jones%40nbn.org.uk&q=lsid%3ANBNSYS0000138878&fq=license%3A%22OGL%22"

    def setup() {
//        Holders.grailsApplication.config.downloads.checklistFacet = "taxon_concept_lsid"
    }

    def cleanup() {
    }

    void "test replace facet=species_guid when config.downloads.checklistFacet defined"() {
        given:
            grailsApplication.config.downloads.checklistFacet = 'taxon_concept_lsid'

        expect:
            controller.replaceFacet(ALA_DOWNLOAD_URL) == NBN_DOWNLOAD_URL
    }

    void "test replace facet=species_guid when config.downloads.checklistFacet is not defined"() {
        given:
        grailsApplication.config.downloads.checklistFacet = null

        expect:
        controller.replaceFacet(ALA_DOWNLOAD_URL) == ALA_DOWNLOAD_URL
    }

    void "test replace facet=species_guid when config.downloads.checklistFacet is species_uid"() {
        given:
        grailsApplication.config.downloads.checklistFacet = "species_guid"

        expect:
        controller.replaceFacet(ALA_DOWNLOAD_URL) == ALA_DOWNLOAD_URL
    }

    void "test options2 for CHECKLIST type"(){
        setup:
        grailsApplication.config.downloads.checklistDownloadUrl = ALA_BASE_DOWNLOAD_URL
        grailsApplication.config.downloads.checklistFacet = 'taxon_concept_lsid'

        def downloadParams = Mock(DownloadParams)
        downloadParams.downloadType >> DownloadType.CHECKLIST.type
        downloadParams.reasonTypeId >> "wotev"
        downloadParams.biocacheDownloadParamString() >> ""
        downloadParams.email >> "a@a.com"


        when:
        controller.options2(downloadParams)
        controller.confirm(downloadParams)

        then:
        controller.chainModel.downloadUrl == NBN_DOWNLOAD_URL

    }

    void "test options2 for FIELDGUIDE type"(){
        setup:
        grailsApplication.config.downloads.fieldguideDownloadUrl = ALA_BASE_DOWNLOAD_URL
        grailsApplication.config.downloads.checklistFacet = 'taxon_concept_lsid'

        controller.downloadService = Mock(DownloadService)
        controller.downloadService.triggerFieldGuideDownload(_) >> ALA_DOWNLOAD_URL2

        def downloadParams = Mock(DownloadParams)
        downloadParams.downloadType >> DownloadType.FIELDGUIDE.type
        downloadParams.reasonTypeId >> "wotev"
        downloadParams.biocacheDownloadParamString() >> ""
        downloadParams.email >> "a@a.com"


        when:
        controller.options2(downloadParams)
        controller.confirm(downloadParams)

        then:
        controller.chainModel.downloadUrl == NBN_DOWNLOAD_URL2
        controller.chainModel.json == NBN_DOWNLOAD_URL2

    }
}

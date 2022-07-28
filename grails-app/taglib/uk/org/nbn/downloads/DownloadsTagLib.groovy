package uk.org.nbn.downloads

class DownloadsTagLib extends au.org.ala.downloads.plugin.DownloadsTagLib {

    static namespace = 'downloads'

    def getLoggerReasons = { attrs ->
        if ((grailsApplication.config.downloads?.reasonDescriptionSplitChar?:'') == '') {
            downloadService.getLoggerReasons()
        } else {
            def beforeSplit = downloadService.getLoggerReasons()
            def afterSplit = []
            beforeSplit.each {
                String[] strNameDesc;
                def afterSplitEl = [:]
                strNameDesc = it['name'].split(grailsApplication.config.downloads.reasonDescriptionSplitChar);
                afterSplitEl.put('id',it['id'])
                afterSplitEl.put('deprecated', it['deprecated'])
                afterSplitEl.put('rkey', it['rkey'])
                afterSplitEl.put('name',strNameDesc[0])
                afterSplitEl.put('description',(strNameDesc.size()>1 ? strNameDesc[1] : ''))
                afterSplit << afterSplitEl

            }
            afterSplit
        }
    }
}

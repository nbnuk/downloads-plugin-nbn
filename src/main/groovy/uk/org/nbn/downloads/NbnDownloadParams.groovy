package uk.org.nbn.downloads

import org.apache.commons.lang.StringUtils
import org.grails.web.util.WebUtils

class NbnDownloadParams extends au.org.ala.downloads.DownloadParams{

    String mapLayoutParams = "" //if downloadType=='map' then this should be populated with e.g. "extents=-14.853515625,50.597186230587035,1.47216796875,58.240163543416415&format=jpg&dpi=300&pradiusmm=0.7&popacity=0.7&pcolour=0D00FB&widthmm=150&scale=on&outline=true&outlineColour=0x000000&baselayer=world&baseMap=&fileName=MyMap.jpg"

    public String biocachedownloadMapParamString() {
        Map paramsMap = mapForPropsWithExcludeList(["searchParams", "targetUri", "downloadType", "downloadFormat", "customClasses", "totalRecords", "dwcHeaders", "includeMisc", "qa", "mapLayoutParams"])
        // space chars are removed via replaceChars, as they cause an URI exception
        String layoutParams = java.net.URLDecoder.decode(mapLayoutParams, "UTF-8")
        String mapLayoutParamsEncoded = URLEncoder.encode(mapLayoutParams, "UTF-8")
        String queryString = WebUtils.toQueryString(paramsMap) + "&" + StringUtils.removeStart(StringUtils.replaceChars(searchParams, " ", "+"), "?") +
                "&mapLayoutParams=" + mapLayoutParamsEncoded
        queryString
    }
}

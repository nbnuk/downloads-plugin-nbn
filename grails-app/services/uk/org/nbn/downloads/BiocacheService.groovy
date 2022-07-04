package uk.org.nbn.downloads

import grails.converters.JSON
import grails.plugin.cache.Cacheable
import grails.transaction.Transactional

import javax.annotation.PostConstruct


class BiocacheService extends au.org.ala.downloads.BiocacheService{

    BiocacheService(grailsApplication){
        super.setGrailsApplication(grailsApplication)
    }

    @PostConstruct
    void init(){
        def ctx = grailsApplication.mainContext
        setGrailsResourceLocator(ctx.getBean("grailsResourceLocator"))
        setUtilityService(ctx.getBean("utilityService"))
        setWebService(ctx.getBean("webService"))
    }


    @Override
    @Cacheable('longTermCache')
    Map getFieldsMap(boolean includeRaw = false) {
        List fields = thisService.getBiocacheFields()
        Map fieldsByClassMap = [:]
        Map classLookup = grailsApplication.config.downloads.classMappings

        fields.each { field ->

            if (field && ((field?.dwcTerm && !(!includeRaw && field.dwcTerm.contains("_raw"))) || ((!(grailsApplication.config.downloads.customOnlyIncludeDwC?:'true').toBoolean()) && !(!includeRaw && field.name.contains("_raw"))))) {
                // only includes "_raw" fields when 'includeRaw' is true
                String classsName = field?.classs ?: "Misc"
                String key = classLookup.get(classsName) ?: "Misc"
                List fieldsList = fieldsByClassMap.get(key) ?: []
                //fieldsList.add(field?.downloadName)
                fieldsList.add([downloadName:field?.downloadName, dwcTerm: field?.dwcTerm])
                fieldsByClassMap.put(key, fieldsList) // overwrites with new list
            }
        }

        log.debug "getFieldsMap(${includeRaw}) -> ${fieldsByClassMap as JSON}"
        fieldsByClassMap
    }
}

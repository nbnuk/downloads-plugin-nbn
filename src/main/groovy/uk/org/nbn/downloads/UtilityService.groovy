package uk.org.nbn.downloads

import grails.transaction.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

import javax.annotation.PostConstruct

//@Transactional
class UtilityService extends au.org.ala.downloads.UtilityService{

    UtilityService(grailsApplication){
        super.setGrailsApplication(grailsApplication)
    }

    @PostConstruct
    void init(){
        def ctx = grailsApplication.mainContext
        setBiocacheService(ctx.getBean("biocacheService"))
    }

    @Override
    List paginateWrapper(List results, GrailsParameterMap params) {
        params.max = params.getInt("max") ?: 20
        params.sort = params.sort ?: 'occurrence_date'
        params.order = params.order ?: (params.dir ?: 'DESC')

        super.paginateWrapper(result, params);
    }
}

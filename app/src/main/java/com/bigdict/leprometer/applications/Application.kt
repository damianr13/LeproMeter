package com.bigdict.leprometer.applications

import com.bigdict.leprometer.data.ApplicationInfoStats


class ProductiveApplication(name: String) : ApplicationInfoStats(name, 0) {

}

class LepraApplication(name: String) : ApplicationInfoStats(name, 0) {

}

class TrackedApplications(
    var productiveApplications: MutableCollection<ProductiveApplication>,
    var lepraApplications: MutableCollection<LepraApplication>
) {
    fun addProductiveApplication(application: ProductiveApplication) {
        this.productiveApplications.add(application)
    }

    fun addLepraApplication(application: LepraApplication) {
        this.lepraApplications.add(application)
    }
}
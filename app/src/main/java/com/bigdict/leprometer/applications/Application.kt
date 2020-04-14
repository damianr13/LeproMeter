package com.bigdict.leprometer.applications

open class Application(var name: String) {
    var time: Int = 0
    var score: Int = 0
    fun addTime(timeToAdd: Int) {
        this.time += timeToAdd
        this.score = this.time * 2
    }

}

class ProductiveApplication(name: String) : Application(name) {

}

class LepraApplication(name: String) : Application(name) {

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
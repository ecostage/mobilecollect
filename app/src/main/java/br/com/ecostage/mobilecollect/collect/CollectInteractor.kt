package br.com.ecostage.mobilecollect.collect

/**
 * Created by cmaia on 7/20/17.
 */
interface CollectInteractor {
    fun save(collect: Collect) : Collect
}
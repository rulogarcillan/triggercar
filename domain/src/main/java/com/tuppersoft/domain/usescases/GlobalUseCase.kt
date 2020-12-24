package com.tuppersoft.domain

abstract class GlobalUseCase<out Type, in Params> where Type : Any? {

    protected abstract suspend fun run(params: Params): Type

    suspend fun invoke(params: Params): Type {
        return run(params)
    }

    class None
}

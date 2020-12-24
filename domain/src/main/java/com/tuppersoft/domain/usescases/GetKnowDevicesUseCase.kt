package com.tuppersoft.domain.usescases

class GetCharacters constructor(private val repository: MarvelRepository) :
    GlobalUseCase<Flow<CharacterData>, Params>() {

    data class Params(val limit: Int = 100, val offset: Int = 0)

    override suspend fun run(params: Params): Flow<CharacterData> {
        return repository.getCharacters(params.limit, params.offset)
    }
}

package com.tuppersoft.squeleton.core.di

import android.bluetooth.BluetoothAdapter
import com.tuppersoft.data.datasource.BluetoothDataSource
import com.tuppersoft.data.datasource.BluetoothDataSourceImpl
import com.tuppersoft.data.repositories.BluetoothRepositoryImpl
import com.tuppersoft.domain.repositories.BluetoothRepository
import com.tuppersoft.domain.usescases.GetKnowDevicesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothAdapter(): BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    @Provides
    @Singleton
    fun provideBluetoothDataSource(adapter: BluetoothAdapter): BluetoothDataSource = BluetoothDataSourceImpl(adapter)

    @Provides
    fun provideBluetoothRepository(datasource: BluetoothDataSource): BluetoothRepository =
        BluetoothRepositoryImpl(datasource)

    @Provides
    fun provideGetKnowDevicesUseCase(repository: BluetoothRepository): GetKnowDevicesUseCase =
        GetKnowDevicesUseCase(repository)
}

package com.tuppersoft.squeleton.core.di

import android.bluetooth.BluetoothAdapter
import android.content.Context
import androidx.room.Room
import com.tuppersoft.data.datasource.BluetoothDataSource
import com.tuppersoft.data.datasource.BluetoothDataSourceImpl
import com.tuppersoft.data.repositories.BluetoothRepositoryImpl
import com.tuppersoft.data.room.TriggerDatabase
import com.tuppersoft.domain.repositories.BluetoothRepository
import com.tuppersoft.domain.usescases.DeleteKnowDeviceUseCase
import com.tuppersoft.domain.usescases.GetKnowDevicesUseCase
import com.tuppersoft.domain.usescases.SaveKnowDeviceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @Singleton
    fun provideBluetoothRepository(
        bluetoothDataSource: BluetoothDataSource,
        bbddDataSource: TriggerDatabase
    ): BluetoothRepository =
        BluetoothRepositoryImpl(bluetoothDataSource, bbddDataSource)

    @Provides
    @Singleton
    fun provideGetKnowDevicesUseCase(repository: BluetoothRepository): GetKnowDevicesUseCase =
        GetKnowDevicesUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveKnowDeviceUseCase(repository: BluetoothRepository): SaveKnowDeviceUseCase =
        SaveKnowDeviceUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteKnowDeviceUseCase(repository: BluetoothRepository): DeleteKnowDeviceUseCase =
        DeleteKnowDeviceUseCase(repository)

    @Provides
    @Singleton
    fun provideChessDatabase(@ApplicationContext appContext: Context): TriggerDatabase = getDatabase(appContext)

    private fun getDatabase(app: Context): TriggerDatabase {
        return Room.databaseBuilder(
            app,
            TriggerDatabase::class.java, "TRIGGER_DATABASE"
        ).fallbackToDestructiveMigration()
            .build()
    }
}

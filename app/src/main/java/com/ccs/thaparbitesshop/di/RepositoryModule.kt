package com.ccs.thaparbitesshop.di

import com.ccs.thaparbitesshop.data.repository.AuthRepository
import com.ccs.thaparbitesshop.data.repository.AuthRepositoryImpl
import com.ccs.thaparbitesshop.data.repository.MenuRepository
import com.ccs.thaparbitesshop.data.repository.MenuRepositoryImpl
import com.ccs.thaparbitesshop.data.repository.OrderRepository
import com.ccs.thaparbitesshop.data.repository.OrderRepositoryImpl
import com.ccs.thaparbitesshop.data.repository.ShopRepository
import com.ccs.thaparbitesshop.data.repository.ShopRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds @Singleton
    abstract fun bindShopRepository(impl: ShopRepositoryImpl): ShopRepository

    @Binds @Singleton
    abstract fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository

    @Binds @Singleton
    abstract fun bindMenuRepository(impl: MenuRepositoryImpl): MenuRepository
}
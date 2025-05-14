import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellerComponent } from './seller.component';
import { SellerRoutingModule } from './seller-routing.module';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [SellerComponent],
  imports: [
    CommonModule,
    SellerRoutingModule,
    ReactiveFormsModule
  ]
})
export class SellerModule {}

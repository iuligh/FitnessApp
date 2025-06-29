import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { MessageModule } from 'primeng/message';
import { MessagesModule } from 'primeng/messages';
import { RippleModule } from 'primeng/ripple';

import { SubscriptionPlansComponent } from './subscription-plans/subscription-plans.component';
import { SubscriptionService } from 'src/app/services/subscription.service';

const routes: Routes = [
  {
    path: 'plans',
    component: SubscriptionPlansComponent
  },
  // Add more subscription-related routes here
];

@NgModule({
  declarations: [
    SubscriptionPlansComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(routes),
    ButtonModule,
    CardModule,
    ProgressSpinnerModule,
    MessageModule,
    MessagesModule,
    RippleModule
  ],
  providers: [
    SubscriptionService
  ]
})
export class SubscriptionModule { }

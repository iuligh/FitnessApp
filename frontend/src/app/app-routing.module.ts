import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./components/auth-components/login/login.component";
import {RegisterComponent} from "./components/auth-components/register/register.component";
import {ProfileComponent} from "./components/auth-components/profile/profile.component";
import {EventsEditComponent} from "./components/events/events-edit/events-edit.component";
import {EventDetailComponent} from "./components/events/event-detail/event-detail.component";
import {EventAddComponent} from "./components/events/event-add/event-add.component";
import {ProgramComponent} from './components/program/program.component';
import {ContactComponent} from "./components/contact/contact.component";
import {SaliComponent} from "./components/sali/sali.component";
import {DetaliuSalaComponent} from "./components/sali/detaliu-sala/detaliu-sala.component";
import { SubscriptionPlansComponent } from './components/subscription/subscription-plans/subscription-plans.component';

const routes: Routes = [
  { path: 'home', component: SaliComponent },
  { path: 'events-add', component: EventAddComponent },
  { path: 'program', component: ProgramComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'events', component: EventsEditComponent },
  { path: 'events/:id', component: EventDetailComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'rooms', component: SaliComponent },
  { path: 'rooms/:id', component: DetaliuSalaComponent },
  { 
    path: 'subscription', 
    loadChildren: () => import('./components/subscription/subscription.module').then(m => m.SubscriptionModule) 
  },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '**', redirectTo: 'home' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

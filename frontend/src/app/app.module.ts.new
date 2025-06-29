import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { RegisterComponent } from './components/auth-components/register/register.component';
import { LoginComponent } from './components/auth-components/login/login.component';
import { ProfileComponent } from './components/auth-components/profile/profile.component';
import { BoardAdminComponent } from './components/auth-components/board-admin/board-admin.component';
import { BoardUserComponent } from './components/auth-components/board-user/board-user.component';
import { EventsEditComponent } from "./components/events/events-edit/events-edit.component";
import { EventDetailComponent } from "./components/events/event-detail/event-detail.component";
import { FooterComponent } from './components/footer/footer.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { EventAddComponent } from './components/events/event-add/event-add.component';
import { ContactComponent } from './components/contact/contact.component';
import { ProgramComponent } from './components/program/program.component';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { TimePickerModule } from '@syncfusion/ej2-angular-calendars';
import { SaliComponent } from './components/sali/sali.component';
import { DetaliuSalaComponent } from './components/sali/detaliu-sala/detaliu-sala.component';
import { FullCalendarModule } from "@fullcalendar/angular";
import { ToastModule } from "primeng/toast";
import { MessageService } from "primeng/api";
import { HttpRequestInterceptor } from './helpers/http.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    ProfileComponent,
    BoardAdminComponent,
    BoardUserComponent,
    EventsEditComponent,
    EventDetailComponent,
    FooterComponent,
    EventAddComponent,
    ContactComponent,
    ProgramComponent,
    SaliComponent,
    DetaliuSalaComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    DropdownModule,
    CalendarModule,
    ReactiveFormsModule,
    TimePickerModule,
    FullCalendarModule,
    ToastModule
  ],
  providers: [
    MessageService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpRequestInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

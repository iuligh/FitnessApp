import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent {
  constructor(private router: Router) {}

  redirectToProgram() {
    window.location.href ='https://etti.upb.ro/';
  }
}

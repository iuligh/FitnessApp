import { Component, OnInit } from '@angular/core';
import { AuthService } from "../../../services/auth.service";
import { StorageService } from "../../../services/storage.service";
import { Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: any = {
    username: null,
    password: null
  };
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];

  constructor(
    private authService: AuthService,
    private storageService: StorageService,
    private router: Router
  ) { }

  ngOnInit(): void {
    if (this.storageService.isLoggedIn()) {
      this.isLoggedIn = true;
      this.roles = this.storageService.getUser().roles;
    }
  }

  onSubmit(): void {
    // Reset error state
    this.isLoginFailed = false;
    this.errorMessage = '';
    
    const { username, password } = this.form;
    
    if (!username || !password) {
      this.handleLoginError('Please enter both username and password');
      return;
    }

    console.log('Attempting login with username:', username);
    
    this.authService.login(username, password).subscribe({
      next: (response: HttpResponse<any>) => {
        console.log('Login successful, response:', response);
        
        // The response body is available in response.body
        const userData = response.body;
        if (userData) {
          console.log('Saving user data:', userData);
          this.authService.saveAuthData(userData);
          this.isLoginFailed = false;
          this.isLoggedIn = true;
          this.roles = this.storageService.getUser()?.roles || [];
          
          // Navigate to the return URL if available, otherwise to home
          const returnUrl = this.router.parseUrl(this.router.url).queryParams['returnUrl'] || '/';
          console.log('Navigating to:', returnUrl);
          this.router.navigateByUrl(returnUrl);
        } else {
          console.error('No user data in response');
          this.handleLoginError('Invalid response from server');
        }
      },
      error: (err: any) => {
        console.error('Login error:', err);
        let errorMessage = 'Login failed. Please check your credentials.';
        
        if (err.status === 0) {
          errorMessage = 'Unable to connect to the server. Please check your connection.';
        } else if (err.error?.message) {
          errorMessage = err.error.message;
        }
        
        this.handleLoginError(errorMessage);
      }
    });
  }

  private handleLoginError(errorMessage: string): void {
    this.errorMessage = errorMessage;
    this.isLoginFailed = true;
  }
}

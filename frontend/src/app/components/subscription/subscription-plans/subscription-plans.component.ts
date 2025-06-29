import { Component, OnInit } from '@angular/core';
import { SubscriptionService, SubscriptionPlan } from 'src/app/services/subscription.service';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { StorageService } from 'src/app/services/storage.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-subscription-plans',
  templateUrl: './subscription-plans.component.html',
  styleUrls: ['./subscription-plans.component.scss']
})
export class SubscriptionPlansComponent implements OnInit {
  plans: SubscriptionPlan[] = [];
  loading = true;

  constructor(
    private subscriptionService: SubscriptionService,
    private messageService: MessageService,
    private router: Router,
    private storageService: StorageService
  ) {}

  ngOnInit(): void {
    this.loadPlans();
  }

  loadPlans(): void {
    this.loading = true;
    this.subscriptionService.getActivePlans().subscribe({
      next: (plans) => {
        this.plans = plans;
        this.loading = false;
        console.log('Successfully loaded', plans.length, 'subscription plans');
      },
      error: (error: any) => {
        console.error('Error loading subscription plans:', error);
        this.loading = false;
        
        let errorMessage = 'Nu s-au putut încărca abonamentele. Vă rugăm încercați din nou mai târziu.';
        
        // Check if it's an HTTP error response
        if (error instanceof HttpErrorResponse) {
          console.error('HTTP Error:', error.status, error.statusText);
          console.error('Error details:', error.error);
          
          // Provide more specific error messages based on status code
          if (error.status === 0) {
            errorMessage = 'Nu s-a putut conecta la server. Verificați conexiunea la internet.';
          } else if (error.status === 500) {
            errorMessage = 'Eroare internă a serverului. Vă rugăm contactați asistența.';
          } else if (error.error?.message) {
            errorMessage = error.error.message;
          }
        } else if (error instanceof Error) {
          // Handle regular Error objects
          errorMessage = error.message || errorMessage;
        }
        
        // Show error message to user
        this.messageService.add({
          severity: 'error',
          summary: 'Eroare',
          detail: errorMessage,
          life: 5000
        });
      }
    });
  }

  selectPlan(plan: SubscriptionPlan): void {
    // Clear any existing messages first
    this.messageService.clear();

    // Check if user is logged in
    if (!this.storageService.isLoggedIn()) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Autentificare necesară',
        detail: 'Pentru a accesa această funcționalitate, vă rugăm să vă autentificați.',
        life: 5000
      });
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/subscription/plans' } });
      return;
    }

    const successUrl = `${window.location.origin}/subscription/success?session_id={CHECKOUT_SESSION_ID}`;
    const cancelUrl = `${window.location.origin}/subscription/cancel`;
    
    // Show loading state
    this.messageService.add({
      severity: 'info',
      summary: 'Se încarcă',
      detail: 'Se pregătește procesul de plată...',
      sticky: true
    });

    this.subscriptionService.createCheckoutSession(plan.id, successUrl, cancelUrl).subscribe({
      next: (session) => {
        this.messageService.clear();
        // Redirect to Stripe Checkout
        window.location.href = session.sessionUrl;
      },
      error: (error) => {
        console.error('Error creating checkout session:', error);
        this.messageService.clear();
        
        let errorMessage = 'Nu s-a putut iniția procesul de plată. Vă rugăm încercați din nou.';
        let navigateToLogin = false;
        let reloadPlans = false;
        
        // Handle different error cases
        if (error.status === 401 || error.message?.toLowerCase().includes('authentication') || 
            error.message?.toLowerCase().includes('token') || error.message?.toLowerCase().includes('credential')) {
          errorMessage = 'Sesiunea a expirat sau nu sunteți autentificat. Vă rugăm să vă autentificați din nou.';
          navigateToLogin = true;
        } else if (error.message?.toLowerCase().includes('invalid price') || error.message?.toLowerCase().includes('plan')) {
          errorMessage = 'Planul selectat nu este disponibil momentan. Vă rugăm alegeți alt plan.';
          reloadPlans = true;
        } else if (error.status === 0) {
          errorMessage = 'Nu s-a putut conecta la server. Verificați conexiunea la internet.';
        } else if (error.status === 500) {
          errorMessage = 'A apărut o eroare pe server. Vă rugăm încercați mai târziu sau contactați asistența.';
        }
        
        // Show error message
        this.messageService.add({
          severity: 'error',
          summary: 'Eroare',
          detail: errorMessage,
          life: 5000
        });
        
        // Navigate to login or reload plans if needed
        if (navigateToLogin) {
          this.router.navigate(['/login'], { queryParams: { returnUrl: '/subscription/plans' } });
        } else if (reloadPlans) {
          this.loadPlans(); // Refresh plans in case of stale data
        }
      }
    });
  }

  getFeatureIcon(feature: string): string {
    const icons: { [key: string]: string } = {
      'Acces nelimitat la sală': 'pi pi-check-circle',
      'Antrenor personal inclus': 'pi pi-user',
      'Acces la toate sălile': 'pi pi-map-marker',
      'Clase de grup incluse': 'pi pi-users',
      'Ședințe cu antrenor personal': 'pi pi-star'
    };
    return icons[feature] || 'pi pi-check';
  }
}

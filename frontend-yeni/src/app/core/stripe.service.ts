import { Injectable } from '@angular/core';

declare var Stripe: any;

@Injectable({
  providedIn: 'root'
})
export class StripeService {
  stripe = Stripe('pk_test_...'); // kendi publishable key’in

  constructor() {}

  createCardElement(elements: any) {
    return elements.create('card');
  }

  mountCard(card: any, elementId: string) {
    card.mount(`#${elementId}`);
  }

  async confirmCardPayment(clientSecret: string, card: any) {
    const result = await this.stripe.confirmCardPayment(clientSecret, {
      payment_method: {
        card: card
      }
    });
    return result;
  }

  getElements() {
    return this.stripe.elements();
  }
}

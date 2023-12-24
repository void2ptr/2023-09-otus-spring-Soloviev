import { ApplicationConfig } from '@angular/core';
import { provideClientHydration } from '@angular/platform-browser';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { HttpClientModule, provideHttpClient, withFetch  } from '@angular/common/http';
import { importProvidersFrom } from '@angular/core';

import { routes } from './app.routes'


export const appConfig: ApplicationConfig = {
  providers: [
    provideClientHydration(),
    provideRouter(routes, withComponentInputBinding()),
    importProvidersFrom(HttpClientModule),
    provideHttpClient(withFetch()),
  ]
};

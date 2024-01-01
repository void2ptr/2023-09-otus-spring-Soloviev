import { ApplicationConfig } from '@angular/core';
import { provideClientHydration } from '@angular/platform-browser';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { HttpClientModule, provideHttpClient, withFetch, HttpClientXsrfModule  } from '@angular/common/http';
import { importProvidersFrom } from '@angular/core';

import { routes } from './app.routes'
import { httpInterceptorProviders } from './services/middleware/http-interceptor-providers'


export const appConfig: ApplicationConfig = {
    providers: [
        provideClientHydration(),
        provideRouter(routes, withComponentInputBinding()),
        importProvidersFrom(HttpClientModule),
        provideHttpClient(withFetch()),
        importProvidersFrom(
            HttpClientXsrfModule.withOptions({
            cookieName: 'Book-Spa-Xsrf-Cookie',
            headerName: 'Book-Spa-Xsrf-Header',
        })),
        httpInterceptorProviders,
    ]
};

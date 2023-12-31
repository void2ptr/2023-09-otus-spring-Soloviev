import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { HttpErrorInterceptorService } from './http-error-interceptor/http-error-interceptor.service'

export const httpInterceptorProviders = [
    { provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptorService, multi: true },
];

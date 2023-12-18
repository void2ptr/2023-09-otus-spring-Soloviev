import { ApplicationConfig } from '@angular/core';
import { provideClientHydration } from '@angular/platform-browser';
import { AuthorsComponent } from './components/authors/authors.component';

export const appConfig: ApplicationConfig = {
  providers: [provideClientHydration()

  ]
};

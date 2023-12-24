import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthorsService {

  constructor() { }

  getAll() {
    return [
      {id: '1', fullName: 'Admin 1111111111111'},
      {id: '2', fullName: 'Admin 2'},
      {id: '3', fullName: 'Admin 33333'}
    ];
  }
}

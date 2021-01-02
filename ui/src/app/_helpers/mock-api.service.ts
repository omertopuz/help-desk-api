import { Injectable } from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse, HTTP_INTERCEPTORS} from '@angular/common/http';
import { Observable,of,throwError } from 'rxjs';
import { delay, mergeMap, materialize, dematerialize } from 'rxjs/operators';
import { USER_ENTITY } from './test-data';

@Injectable({
  providedIn: 'root'
})
export class MockApiService implements HttpInterceptor {

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const { url, method, headers, body } = req;
    return of(null)
    .pipe(mergeMap(handleRoute))
    .pipe(materialize()) // call materialize and dematerialize to ensure delay even if an error is thrown (https://github.com/Reactive-Extensions/RxJS/issues/648)
    .pipe(delay(500))
    .pipe(dematerialize());

    function handleRoute() {
      switch (true) {
          case url.endsWith('/users/authenticate') && method === 'POST':
              return authenticate();
          default:
              // pass through any requests not handled above
              return next.handle(req);
      }

      // route functions

      function authenticate() {
        const { username, password } = body;
        const user = USER_ENTITY;
        if (!user) return error('Username or password is incorrect');
        return ok({
            id: user.userId,
            username: user.username,
            firstName: user.firstName,
            lastName: user.lastName,
            role: user.role,
            token: `fake-jwt-token.${user.userId}`
        });
    }

    function error(message) {
      return throwError({ status: 400, error: { message } });
  }
  
  function ok(body) {
    return of(new HttpResponse({ status: 200, body }));
}

  }
    

  

  }
}


export const mockApiProvider = {
  // use fake backend in place of Http service for backend-less development
  provide: HTTP_INTERCEPTORS,
  useClass: MockApiService,
  multi: true
};
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserFilesService {
  private baseUrl = 'http://localhost:8080';
  constructor(private http: HttpClient) { }
  upload(file: File, user: string): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();
    formData.append('files', file);
    const req = new HttpRequest('POST', `${this.baseUrl}/api/files/upload/${user}`, formData, {
      reportProgress: true,
      responseType: 'json'
    });
    return this.http.request(req);
  }
  getFiles(user: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/api/files/${user}`);
  }
  deleteFiles(user: string, filename: string) {
    return this.http.delete(`${this.baseUrl}/api/files/${user}/${filename}`, {observe: 'response'});
  }
}

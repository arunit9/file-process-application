import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {
  private baseUrl = 'http://localhost:8080';
  constructor(private http: HttpClient) { }
  // getFileStatistics(file: File): Observable<HttpEvent<any>> {
  //   const formData: FormData = new FormData();
  //   formData.append('files', file);
  //   const req = new HttpRequest('POST', `${this.baseUrl}/api/files/upload/arunit`, formData, {
  //     reportProgress: true,
  //     responseType: 'json'
  //   });
  //   return this.http.request(req);
  // }
  getStatistics(filename: String, user: string): Observable<any> {
    if(typeof filename!='undefined' && filename){
      return this.http.get(`${this.baseUrl}/api/statistics/${user}/${filename}`);
    }
    return this.http.get(`${this.baseUrl}/api/statistics/${user}`);
  }
}
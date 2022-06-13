import { Component, OnInit } from '@angular/core';
import { HttpEventType, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserFilesService } from '../../services/user-files.service';
import { TokenStorageService } from '../../services/token-storage.service';

@Component({
  selector: 'app-user-files',
  templateUrl: './user-files.component.html',
  styleUrls: ['./user-files.component.css']
})
export class UserFilesComponent implements OnInit {
  selectedFiles?: FileList;
  progressInfos: any[] = [];
  message: string[] = [];
  userFileList?: Observable<any>;
  searchText = "";
  deleteError = "";
  isDeleteError = false;
  isLoggedIn = false;
  currentUser: any;
  constructor(private uploadService: UserFilesService, private token: TokenStorageService) { }
  
  ngOnInit(): void {
    this.isLoggedIn = !!this.token.getToken();
    if (this.isLoggedIn) {
      const user = this.token.getUser();
      this.currentUser = user.username;
    }
    this.userFileList = this.uploadService.getFiles(this.currentUser);
  }

  refresh(): void {
    this.isLoggedIn = !!this.token.getToken();
    if (this.isLoggedIn) {
      const user = this.token.getUser();
      this.currentUser = user.username;
    }
    this.userFileList = this.uploadService.getFiles(this.currentUser);
  }

  selectFiles(event: any): void {
    this.message = [];
    this.progressInfos = [];
    this.selectedFiles = event.target.files;
  }

  uploadFiles(): void {
    this.message = [];
    if (this.selectedFiles) {
      for (let i = 0; i < this.selectedFiles.length; i++) {
        this.upload(i, this.selectedFiles[i]);
      }
    }
    this.userFileList = this.uploadService.getFiles(this.currentUser);
  }

  upload(idx: number, file: File): void {
    this.progressInfos[idx] = { value: 0, fileName: file.name };
    if (file) {
      this.uploadService.upload(file, this.currentUser).subscribe(
        (event: any) => {
          if (event.type === HttpEventType.UploadProgress) {
            this.progressInfos[idx].value = Math.round(100 * event.loaded / event.total);
          } else if (event instanceof HttpResponse) {
            const msg = 'Upload Successful: ' + file.name;
            this.message.push(msg);
            this.userFileList = this.uploadService.getFiles(this.currentUser);
          }
        },
        (err: any) => {
          this.progressInfos[idx].value = 0;
          const msg = 'Upload Failed: ' + file.name;
          this.message.push(msg);
          this.userFileList = this.uploadService.getFiles(this.currentUser);
        }
      );
    }
  }
  deleteFiles(file: string): void {
    this.uploadService.deleteFiles(this.currentUser, file).subscribe(
      response => {
        if (response.status == 204) {
          this.userFileList  = this.uploadService.getFiles(this.currentUser)
        } else {
          this.isDeleteError = true;
          this.deleteError = "Delete Failed";
        }
      }
    );
    this.userFileList = this.uploadService.getFiles(this.currentUser);
  }
}

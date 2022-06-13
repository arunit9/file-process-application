import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { StatisticsService } from 'src/app/services/statistics.service';
import { UserFilesService } from '../../services/user-files.service';
import { TokenStorageService } from '../../services/token-storage.service';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {

  selectedFile?: File;
  statistics?: Observable<any>;
  userFileList?: Observable<any>;
  searchText = "";
  isLoggedIn = false;
  currentUser: any;
  selected = "All";
  //fileForm!: FormGroup;
  constructor(private statisticsService: StatisticsService, 
    private uploadService: UserFilesService, 
    private token: TokenStorageService) { }

  ngOnInit(): void {
    // this.fileForm = this.fb.group({
    //   fileSelector: [null]
    // });
    this.isLoggedIn = !!this.token.getToken();
    if (this.isLoggedIn) {
      const user = this.token.getUser();
      this.currentUser = user.username;
    }
    this.userFileList = this.uploadService.getFiles(this.currentUser);
    if (this.selected == "All") {
      this.statistics = this.statisticsService.getStatistics("", this.currentUser);
    } else {
      this.statistics = this.statisticsService.getStatistics(this.selected, this.currentUser);
    }
  }

  // submit() {
  //   console.log("Form Submitted")
  //   console.log(this.selected)
  //   if (this.selected == "All") {
  //     console.log("this.selected is ALL")
  //     this.statistics = this.statisticsService.getStatistics("", this.currentUser);
  //   } else {
  //     console.log("this.selected is filename")
  //     this.statistics = this.statisticsService.getStatistics(this.selected, this.currentUser);
  //   }
  // }

  update(event: any) {
    this.selected = event.target.value;
    if (this.selected == "All") {
      console.log("this.selected is ALL")
      this.statistics = this.statisticsService.getStatistics("", this.currentUser);
    } else {
      console.log("this.selected is filename")
      this.statistics = this.statisticsService.getStatistics(this.selected, this.currentUser);
    }
  }

}

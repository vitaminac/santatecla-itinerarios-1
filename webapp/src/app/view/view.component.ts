import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { View } from '../model/view';
import { File } from '../model/file';
import { ResourcesService } from '../service/resources.service';
import { Unit } from '../model/unit';
import { AuthenticationService } from '../service/authentication.service';

@Component({
    selector: 'app-view',
    templateUrl: './view.component.html',
    styleUrls: ['./view.component.scss']
})
export class ViewComponent implements OnInit {

    @Input()
    view: View;

    @Output()
    delete: EventEmitter<any>;

    files: File[];
    units: Unit[];
    candidates: File[];

    constructor(
        private rest: ResourcesService,
        private auth: AuthenticationService
    ) {
        this.delete = new EventEmitter();
    }

    ngOnInit() {
        this.view = new View(this.view, this.rest);
        this.view.files.subscribe(files => this.files = files);
        this.rest.fetchUnits().subscribe(units => this.units = units);
    }

    deleteView() {
        this.delete.emit();
    }

    addFileReference(file: File) {
        this.rest.addResourceRelation(this.view, file, 'forms').subscribe(() => {
            if (this.files.every(f => f.id !== file.id)) {
                this.files.push(file);
            }
        });
    }

    deleteFileReference(file: File) {
        this.rest.deleteResourceRelation(this.view, '/forms/' + file.id)
            .subscribe(() => this.files = this.files.filter(f => f.id !== file.id));
    }

    selectUnit(unit: Unit) {
        this.rest.fetchFiles(unit.id).subscribe(files => this.candidates = files);
    }

    get isAdmin() {
        return this.auth.isAdmin;
    }
}

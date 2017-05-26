import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { AdminTools } from './admin-tools.model';
import { AdminToolsService } from './admin-tools.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-admin-tools',
    templateUrl: './admin-tools.component.html'
})
export class AdminToolsComponent implements OnInit, OnDestroy {
adminTools: AdminTools[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private adminToolsService: AdminToolsService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.adminToolsService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.adminTools = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.adminToolsService.query().subscribe(
            (res: ResponseWrapper) => {
                this.adminTools = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInAdminTools();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: AdminTools) {
        return item.id;
    }
    registerChangeInAdminTools() {
        this.eventSubscriber = this.eventManager.subscribe('adminToolsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

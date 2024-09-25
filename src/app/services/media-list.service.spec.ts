import { TestBed } from '@angular/core/testing';

import { MediaListService } from './media-list.service';

describe('MediaListService', () => {
  let service: MediaListService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MediaListService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

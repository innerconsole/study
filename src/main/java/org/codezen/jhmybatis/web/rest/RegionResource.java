package org.codezen.jhmybatis.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import org.codezen.jhmybatis.service.mybatis.CMap;
import org.codezen.jhmybatis.service.mybatis.RegionService;
import org.codezen.jhmybatis.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.jhipster.web.util.HeaderUtil;
/**
 * REST controller for managing {@link org.codezen.jhmybatis.domain.Region}.
 */
@RestController
@RequestMapping("/api")
public class RegionResource {

    private final Logger log = LoggerFactory.getLogger(RegionResource.class);

    private static final String ENTITY_NAME = "region";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegionService regionService;

    public RegionResource(RegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * {@code POST  /regions} : Create a new region.
     *
     * @param map the region to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new region, or with status {@code 400 (Bad Request)} if the region has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/regions")
    public ResponseEntity<?> createRegion(@RequestBody HashMap<String, Object> map) throws URISyntaxException {
        log.debug("REST request to save Region : {}", map);
        //if (region.getId() != null) {
        if (map.get("id") != null) {
            throw new BadRequestAlertException("A new region cannot already have an ID", ENTITY_NAME, "idexists");
        }
        regionService.insert(map);

        return ResponseEntity.created(new URI("/api/regions/" + map.get("id").toString()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, map.get("id").toString()))
            .body(map);
    }

    /**
     * {@code PUT  /regions} : Updates an existing region.
     *
     * @param map the region to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated region,
     * or with status {@code 400 (Bad Request)} if the region is not valid,
     * or with status {@code 500 (Internal Server Error)} if the region couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/regions")
    @Transactional
    public ResponseEntity<?> updateRegion(@RequestBody HashMap<String, Object> map) throws URISyntaxException {
        log.debug("REST request to update Region : {}", map);
        if (map.get("id") == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        regionService.update(map);

        // 해당 region 의 country 가 있는지 확인
        int cnt = regionService.ifExists(map);

        if (cnt > 0) {
            log.debug("Country exists in the Region");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            // TODO
            // Need client work
        }

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, map.get("id").toString()))
            .body(null);
    }

    /**
     * {@code GET  /regions} : get all the regions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of regions in body.
     */
    @GetMapping("/regions")
    public List<CMap<String, Object>> getAllRegions() {
        log.debug("REST request to get all Regions");
        List<CMap<String, Object>> regions = regionService.findAll();
        return regions;
    }

    /**
     * {@code GET  /regions/:id} : get the "id" region.
     *
     * @param id the id of the region to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the region, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/regions/{id}")
    public HashMap<String, Object> getRegion(@PathVariable Long id) {
        log.debug("REST request to get Region : {}", id);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",  id+"");
        HashMap<String, Object> region = regionService.findOne(map);
        return region;
    }

    /**
     * {@code DELETE  /regions/:id} : delete the "id" region.
     *
     * @param id the id of the region to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/regions/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        log.debug("REST request to delete Region : {}", id);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",  id+"");
        regionService.delete(map);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

}

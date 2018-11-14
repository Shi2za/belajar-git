/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefFixedTangibleAssetDepreciationStatus;
import hotelfx.persistence.model.RefFixedTangibleAssetType;
import hotelfx.persistence.model.TblFixedTangibleAsset;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FAssetManager {
    public TblFixedTangibleAsset insertDataAsset(TblFixedTangibleAsset asset);
    public boolean updateDataAsset(TblFixedTangibleAsset asset);
    public boolean deleteDataAsset(TblFixedTangibleAsset asset);
    public TblFixedTangibleAsset getAsset(long id);
    public List<TblFixedTangibleAsset> getAllDataAsset();
    public List<RefFixedTangibleAssetType>getAllAssetType();
    public List<RefFixedTangibleAssetDepreciationStatus>getAllAssetDepreciationStatus();
    //-----------------------------------------------------//
    public String getErrorMessage();
}

package hk.org.ha.eclaim.bs.request.helper;

import java.util.List;

import hk.org.ha.eclaim.bs.request.constant.PostConstant;

public class PostHelper {

	public static String getSegment5(List<String> annualPlanInd, List<String> programType) {
		if (annualPlanInd.size() == 1) {
			return programType.get(0);
		}
		else {
			boolean containSRRS = false;
			boolean containLR = false;
			
			// 1. If one of the Funding Sources is related to RAE, Segment 5 of Post-ID will become “RAE”
			for (int i=0; i<programType.size(); i++) {
				if ("RAE".equals(programType.get(i))) {
					return "RAE"; 
				}
				
				if ("S".equals(programType.get(i))) {
					containSRRS = true;
				}
				
				if ("LR".equals(programType.get(i))) {
					containLR = true;
				}
			}
			
			// 2. If no Funding Source is related to RAE, but one of the Funding Sources is related to Special Program / SRRS, Segment 5 of Post-ID will become “S”
			if (containSRRS) {
				return "S";
			}

			// 3. If no Funding Source is related to RAE & S, but one of the Funding Sources is related to Limited Registration, Segment 5 of Post-ID will become “LR”
			if (containLR) {
				return "LR";
			}

			// 4. If all Funding Source are related to Baseline, Segment 5 of Post-ID will become “BL”
			return "BL";
		}
	}

	public static String getSegment6(String postDuration, String postFTEType) {
		if (PostConstant.POST_DUR_VALUE_RECURRENT.equals(postDuration)) {
			if (PostConstant.POST_FTE_TYPE_FULL_TIME.equals(postFTEType)) {
				return PostConstant.POST_ID_SEGMENT_6_RECURRENT;
			} else {
				return PostConstant.POST_ID_SEGMENT_6_PERM_PART_TIME;
			}
		} else if (PostConstant.POST_DUR_VALUE_TL_CONTRACT.equals(postDuration)) {
			if (PostConstant.POST_FTE_TYPE_FULL_TIME.equals(postFTEType)) {
				return PostConstant.POST_ID_SEGMENT_6_TL_CONTRACT_FULL_TIME;
			} else {
				return PostConstant.POST_ID_SEGMENT_6_TL_CONTRACT_PART_TIME;
			}
		} else {
			if (PostConstant.POST_FTE_TYPE_FULL_TIME.equals(postFTEType)) {
				return PostConstant.POST_ID_SEGMENT_6_TL_TEMP_FULL_TIME;
			} else {
				return PostConstant.POST_ID_SEGMENT_6_TL_TEMP_PART_TIME;
			}
		}
	}
	
	public static String getSegment7(String proposedPostId) {
		return proposedPostId.substring(proposedPostId.lastIndexOf("-")+1);
	}
	
}

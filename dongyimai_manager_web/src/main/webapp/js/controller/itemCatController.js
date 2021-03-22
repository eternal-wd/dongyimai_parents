 //商品类目控制层 
app.controller('itemCatController' ,function($scope,$controller,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承

	$scope.parentId=0;//上级ID
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			$scope.entity.parentId=$scope.parentId;//赋予上级ID
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	/*$scope.reloadList();//重新加载*/
					$scope.findByParentId($scope.parentId);//重新加载

				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					/*$scope.reloadList();//刷新列表*/
					$scope.findByParentId($scope.parentId);//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//根据上级ID显示下级列表
	$scope.findByParentId=function(parentId){
		$scope.parentId=parentId;//记住上级ID
		itemCatService.findByParentId(parentId).success(
			function(response){
				$scope.list=response;
			}
		);
	}

	$scope.grade=1;//默认为1级
	//设置级别
	$scope.setGrade=function(value){
		$scope.grade=value;
	}
	//读取列表
	// 页面加载时最先在body调用findByParentId(), 此时呈现了一级页面,并填充了entity
	// 初次使用时,面包屑不应该有二级和三级显示, 所以entity_x=null 页面中就取不到{{entity_x.name}}
	// 首次进入二级一定是点击的”查询下级”, 而不是面包屑.  此时会填充 entity_1数据(填充了一级页面的entity数据)
	$scope.selectList=function(p_entity){
		if($scope.grade==1){//如果为1级
			$scope.entity_1=null;
			$scope.entity_2=null;
		}
		if($scope.grade==2){//如果为2级
			$scope.entity_1=p_entity;
			$scope.entity_2=null;
		}
		if($scope.grade==3){//如果为3级
			$scope.entity_2=p_entity;
		}
		$scope.findByParentId(p_entity.id);    //查询此级下级列表
	}

	$scope.typeTemplateList={data:[]};
	$scope.findtypeTemplateList=function (){
		typeTemplateService.selectOptionList().success(
			function (response){
				$scope.typeTemplateList={data: response};
			}
		);
	}
});	
<!--Success Modal Templates-->
    <div id="modal-success" class="modal modal-message modal-success fade" style="display: none;" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <i class="glyphicon glyphicon-check"></i>
                </div>
                <div class="modal-title" id="modal-title-success">Success</div>

<!--                 <div class="modal-body">You have done great!</div> -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-success" data-dismiss="modal">OK</button>
                </div>
            </div> <!-- / .modal-content -->
        </div> <!-- / .modal-dialog -->
    </div>
    <!--End Success Modal Templates-->
    <!--Info Modal Templates-->
    <div id="modal-info" class="modal modal-message modal-info fade" style="display: none;" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <i class="fa fa-envelope"></i>
                </div>
                <div class="modal-title" id="modal-title-info">Alert</div>

<!--                 <div class="modal-body">You'vd got mail!</div> -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-info" data-dismiss="modal">OK</button>
                </div>
            </div> <!-- / .modal-content -->
        </div> <!-- / .modal-dialog -->
    </div>
    <!--End Info Modal Templates-->
    <!--Danger Modal Templates-->
    <div id="modal-danger" class="modal modal-message modal-danger fade" style="display: none;" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <i class="glyphicon glyphicon-fire"></i>
                </div>
                <div class="modal-title" id="modal-title-danger">Alert</div>

<!--                 <div class="modal-body">You'vd done bad!</div> -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal">OK</button>
                </div>
            </div> <!-- / .modal-content -->
        </div> <!-- / .modal-dialog -->
    </div>
    <!--End Danger Modal Templates-->
    <!--Danger Modal Templates-->
    <div id="modal-warning" class="modal modal-message modal-warning fade" style="display: none;" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <i class="fa fa-warning"></i>
                </div>
                <div class="modal-title" id="modal-title-warning">Warning</div>

<!--                 <div class="modal-body">Is something wrong?</div> -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-warning" data-dismiss="modal">OK</button>
                </div>
            </div> <!-- / .modal-content -->
        </div> <!-- / .modal-dialog -->
    </div>
    <!--End Danger Modal Templates-->
    
    <script>
    	function successAlert(message){
    		$('#modal-title-success').html(message);
    		$('#modal-success').modal('toggle')
    	}
    	
    	function infoAlert(message){
    		$('#modal-title-info').html(message);
    		$('#modal-info').modal('toggle')
    	}
    	
    	function dangerAlert(message){
    		$('#modal-title-danger').html(message);
    		$('#modal-danger').modal('toggle')
    	}
    	
    	function warningAlert(message){
    		$('#modal-title-warning').html(message);
    		$('#modal-warning').modal('toggle')
    	}
    </script>
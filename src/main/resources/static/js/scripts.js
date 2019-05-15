/**
 * 
 */

function checkBillingAddress() {
    if ($("#theSameAsShippingAddress").is(":checked")) {
        $(".billingAddress").prop("disabled", true);
    } else {
        $(".billingAddress").prop("disabled", false);
    }
}

function checkPasswordMatch() {
    var password = $("#txtNewPassword").val();
    var confirmPassword = $("#txtConfirmPassword").val();
    if (password == "" && confirmPassword == "") {
        $("#checkPasswordMatch").html("");
        $("#updateUserInfoButton").prop('disabled', false);
    } else {
        if (password != confirmPassword) {
            $("#checkPasswordMatch").html("Passwords do not match!");
            $("#updateUserInfoButton").prop('disabled', true);
        } else {
            $("#checkPasswordMatch").html("Passwords match");
            $("#updateUserInfoButton").prop('disabled', false);
        }
    }
}

$(document).ready(function () {
    $(".cartItemQty").on('change', function () {
        var id = this.id;
        $('#update-item-' + id).css('display', 'inline-block');
    });
    $("#theSameAsShippingAddress").on('click', checkBillingAddress);
    $("#txtConfirmPassword").keyup(checkPasswordMatch);
    $("#txtNewPassword").keyup(checkPasswordMatch);
});
//$(document).ready(function(){
//    if($("#star5").is(":checked")){
//        $('#add-rating-').css('display', 'inline-block');
//    }

//	$(".fff").on('click', function(){
//		var id=this.id;
//		
//		$('#add-rating-').css('display', 'inline-block');
//	});
//	$("#theSameAsShippingAddress").on('click', checkBillingAddress);
//	$("#txtConfirmPassword").keyup(checkPasswordMatch);
//	$("#txtNewPassword").keyup(checkPasswordMatch);
//});

$(document).ready(function () {
    $('#list').click(function (event) {
        event.preventDefault();
        $('#products .item').addClass('list-group-item');
        $('.watchspace').css('height','295px');
//        document.getElementById("product").style.height = '300px;';
//        $('#product').style.height = '300px;';
    });
    $('#grid').click(function (event) {
        event.preventDefault();
        $('#products .item').removeClass('list-group-item');
        $('.watchspace').css('height','700px');
//        document.getElementById("product").style.height = '700px;';
        $('#products .item').addClass('grid-group-item');
    });
});
$(document).ready(function () {
    $('.delete-watch').on ('click', function () {
        /*<![CDATA[*/
        var path = /*[[@{/}]]*/'remove';
        /*]]>*/

        var id = $(this).attr('id');
                console.log(id);


        bootbox.confirm({
            message: "Are you sure to remove this watch? It can't be undone.",
            buttons: {
                cancel: {
                    label: '<i class="fa fa-times"></i> Cancel'
                },
                confirm: {
                    label: '<i class="fa fa-check"></i> Confirm'
                }
            },
            callback: function (confirmed) {
                if (confirmed) {
                    $.post(path, {'id': id}, function (res) {
                        location.reload();
                    });
                }
            }
        });
    });

    $('#deleteSelected').click(function () {
        var idList = $('.checkboxWatch');
        var watchIdList = [];
        for (var i = 0; i < idList.length; i++) {
            if (idList[i].checked == true) {
                watchIdList.push(idList[i]['id'])
            }
        }

//        console.log(watchIdList);

        /*<![CDATA[*/
        var path = /*[[@{/}]]*/'removeList';
        /*]]>*/

        bootbox.confirm({
            message: "Are you sure to remove all selected watches? It can't be undone.",
            buttons: {
                cancel: {
                    label: '<i class="fa fa-times"></i> Cancel'
                },
                confirm: {
                    label: '<i class="fa fa-check"></i> Confirm'
                }
            },
            callback: function (confirmed) {
                if (confirmed) {
                    $.ajax({
                        type: 'POST',
                        url: path,
                        data: JSON.stringify(watchIdList),
                        contentType: "application/json",
                        success: function (res) {
                            console.log(res);
//                            location.reload()
                        },
                        error: function (res) {
                            console.log(res);
//                            location.reload();
                        }
                    });
                }
            }
        });
    });

    $("#selectAllWatches").click(function () {
        if ($(this).prop("checked") == true) {
            $(".checkboxWatch").prop("checked", true);
        } else if ($(this).prop("checked") == false) {
            $(".checkboxWatch").prop("checked", false);
        }
    })
});
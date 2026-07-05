package com.example.sharestreet.presentation.Friends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sharestreet.ViewModels.FriendRequestViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sharestreet.domainLayer.model.RequestWithUser
import com.example.sharestreet.domainLayer.model.UserModel

@Composable
fun FriendsScreen(
    friendRequestViewModel: FriendRequestViewModel = hiltViewModel()
){
    val friendReqList by friendRequestViewModel.receivedRequest.collectAsState()
    var reqList by remember{
        mutableStateOf(friendReqList)
    }
    FriendReqList(reqList,OnRemove={uid->
        reqList?.filter {
            it?.user?.uid!=uid
        }
    },
        acceptRejectRequest = {a,b->
            friendRequestViewModel.acceptRejectReq(a,b)
        })
}
@Composable
fun FriendReqList(
    friendRequestList:List<RequestWithUser>?,
    OnRemove:(String)->Unit,
    acceptRejectRequest:(String,String)-> Unit
){

    Text(
        "Pending Requests"
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ){
        items(friendRequestList!!.size){idx->
            reqCard(
                friendRequestList[idx].user,
                OnRemove,
                acceptRejectRequest = {it->
                    acceptRejectRequest(friendRequestList[idx].requestId,it)
                }
            )
        }
    }
}
@Composable
fun reqCard(
    reqItem: UserModel?,
    OnRemove: (String) -> Unit,
    acceptRejectRequest: (String) -> Unit
){
    Card(
        modifier = Modifier.padding(12.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row() {
            Text("${reqItem?.displayName}")
            Button(onClick = {
                if(reqItem!=null){
                    OnRemove(reqItem.uid)
                    acceptRejectRequest("Accept")
                }
                //Accept
            }) {
                Text("Accept")
            }
            Button(onClick = {
                if(reqItem!=null){
                    OnRemove(reqItem.uid)
                    acceptRejectRequest("Reject")
                }
                //Reject
            }) {
                Text("Reject")
            }
        }
    }
}